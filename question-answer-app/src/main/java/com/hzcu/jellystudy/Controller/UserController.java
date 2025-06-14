package com.hzcu.jellystudy.Controller;

import com.hzcu.jellystudy.Api.UserService;
import com.hzcu.jellystudy.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    // 用于记录用户上次登录时间，防止同一天多次登录获取奖励
    private final Map<String, Date> lastLoginRewardMap = new ConcurrentHashMap<>();
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * 用户每日登录接口，每日首次登录增加1单位money
     * @param userId 用户ID
     * @return 登录结果
     */
    @PostMapping("/{userId}/daily-login")
    public ResponseEntity<?> dailyLogin(@PathVariable("userId") String userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("用户不存在");
        }
        
        User user = userOptional.get();
        Date now = new Date();
        
        // 更新用户最后登录时间
        user = userService.updateLastLogin(userId);
        
        // 检查是否已经在今天获得过登录奖励
        boolean isRewardEligible = true;
        if (lastLoginRewardMap.containsKey(userId)) {
            Date lastRewardDate = lastLoginRewardMap.get(userId);
            isRewardEligible = !isSameDay(lastRewardDate, now);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("lastLogin", user.getLastLogin());
        
        // 如果符合奖励条件，增加1单位money
        if (isRewardEligible) {
            user = userService.addMoney(userId, 1);
            lastLoginRewardMap.put(userId, now);
            response.put("dailyReward", true);
            response.put("moneyAdded", 1);
            response.put("currentMoney", user.getMoney());
            response.put("message", "每日登录奖励已发放，获得1单位money");
        } else {
            response.put("dailyReward", false);
            response.put("currentMoney", user.getMoney());
            response.put("message", "今日已领取登录奖励，明天再来吧");
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取用户余额
     * @param userId 用户ID
     * @return 用户余额信息
     */
    @GetMapping("/{userId}/balance")
    public ResponseEntity<?> getUserBalance(@PathVariable("userId") String userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("用户不存在");
        }
        
        User user = userOptional.get();
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("balance", user.getMoney());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 判断两个日期是否为同一天
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 是否为同一天
     */
    private boolean isSameDay(Date date1, Date date2) {
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        cal1.setTime(date1);
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH) &&
                cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH);
    }
}
