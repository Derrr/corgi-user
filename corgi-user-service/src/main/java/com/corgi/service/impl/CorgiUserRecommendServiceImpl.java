package com.corgi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.corgi.mapper.CorgiFeedMapper;
import com.corgi.mapper.CorgiUserRecommendMapper;
import com.corgi.user.api.CorgiUserRecommendService;
import com.corgi.user.entity.UserDetail;
import com.corgi.user.entity.UserPosition;
import com.corgi.user.entity.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author tairanliu
 */
@Service(interfaceClass = CorgiUserRecommendService.class)
@Slf4j
@Component
public class CorgiUserRecommendServiceImpl implements CorgiUserRecommendService {
    @Autowired
    private CorgiUserRecommendMapper corgiUserRecommendMapper;
    @Autowired
    private CorgiFeedMapper corgiFeedMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static List<String> groupOrder = Arrays.asList("偏瘦", "偏胖", "肌肉", "肉壮", "精壮", "匀称");


    @Override
    public void clearRecUser(String userId) {
        log.info("testing... clear rec user:{} ", userId);
        corgiUserRecommendMapper.clearRecUsers(userId);
    }

    @Override
    public void followRecUser(String userId, String recId) {
        corgiUserRecommendMapper.insertUserRecommend(userId, recId);
        corgiUserRecommendMapper.updateStatus(userId, recId, "1", "0");
    }

    @Override
    public void addRecUser(String userId, String recId, Double weight) {
        log.info("testing... add rec user:{},{},{} ", userId, recId, weight);
        try {
            if (weight == null) {
                weight = 1.0;
            }
            corgiUserRecommendMapper.insertUserRecommend(userId, recId);
            corgiUserRecommendMapper.addRecommend(userId, recId, weight);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void updateRecStatus(String userId, String recId, String status) {
        corgiUserRecommendMapper.updateStatus(userId, recId, status, System.currentTimeMillis() + "");
    }

    @Override
    public List<UserProfile> getRecUser(String userId, Integer size) {
        Long time = System.currentTimeMillis();
        time = time - 3 * 24 * 3600 * 1000;
        return corgiUserRecommendMapper.getRecUsers(userId, size, time);
    }

    @Override
    public List<UserProfile> getVlogRecUser(String userId, Integer size) {
        List<UserProfile> userProfiles = corgiUserRecommendMapper.getVlogRecUsers(userId, size);
        if (CollectionUtils.isEmpty(userProfiles)) {
            userProfiles = new ArrayList<>();
            List<String> userIds = corgiFeedMapper.getPopularUserIds();
            for (String popularIds : userIds) {
                UserProfile profile = new UserProfile();
                profile.setUserId(popularIds);
                userProfiles.add(profile);
            }
        }
        return userProfiles;
    }

    @Override
    public List<UserProfile> getInfluencerByCity(String userId, String city, Integer size) {
        List<UserProfile> userProfiles = corgiUserRecommendMapper.getCityInfluencer(city, userId, size);
        if (userProfiles.size() < size) {
            List<UserProfile> nationalProfiles = corgiUserRecommendMapper.getNotCityInfluencer(city, userId, size - userProfiles.size());
            userProfiles.addAll(nationalProfiles);
        }
        for (UserProfile profile : userProfiles) {
            if (StringUtils.isEmpty(profile.getAvatar())) {
                continue;
            }
            profile.setAvatar(profile.getAvatar().replaceAll("corgi-pic\\.oss-cn-beijing\\.aliyuncs\\.com", "image.corgi.org.cn").replaceAll("https://","http://"));
        }
        return userProfiles;
    }

    @Override
    public List<UserProfile> getCityPopulate(String userId, String city, Integer page, Integer size) {
        Long time = System.currentTimeMillis();
        time -= 3 * 24 * 3600 * 1000;
        List<UserProfile> userProfiles =  corgiUserRecommendMapper.getCityPopulate(city, userId, time, (page - 1) * size, size);
        for (UserProfile profile : userProfiles) {
            if (StringUtils.isEmpty(profile.getAvatar())) {
                continue;
            }
            profile.setAvatar(profile.getAvatar().replaceAll("corgi-pic\\.oss-cn-beijing\\.aliyuncs\\.com", "image.corgi.org.cn").replaceAll("https://","http://"));
        }
        return userProfiles;

    }

    @Override
    public void deleteRecUserByWeight(String userId, Integer weight) {
        corgiUserRecommendMapper.deleteByWeight(userId, weight);
    }

    @Override
    public void distLikeUser(String userId, String disLikeUserId) {
        corgiUserRecommendMapper.insertUserRecommend(userId, disLikeUserId);
        corgiUserRecommendMapper.updateStatus(userId, disLikeUserId, "2", System.currentTimeMillis() + "");
    }

    @Override
    public void initInfluencer() {
        corgiUserRecommendMapper.clearInfluencerBillboard();
        corgiUserRecommendMapper.initInfluencerBillboard();
    }

    @Override
    public List<String> getCityRecommendImage(String userId, String city, Integer page, Integer pageSize) {
        return corgiUserRecommendMapper.getCityImage(city, userId, (page - 1) * pageSize, pageSize);
    }

    @Override
    public List<String> getNotCityRecommendImage(String userId, String city, Integer page, Integer pageSize) {
        return corgiUserRecommendMapper.getNotCityImage(city, userId, (page - 1) * pageSize, pageSize);
    }

    @Override
    public void clearRecActivity(String userId) {
        corgiUserRecommendMapper.clearRecActivity(userId);
    }

    @Override
    public void addRecActivity(String userId, String recId, Double weight) {
        corgiUserRecommendMapper.addRecommendActivity(userId, recId);
        corgiUserRecommendMapper.updateRecommendActivity(userId, recId, weight);
    }

    @Override
    public void addGroupCor(String userId, String group) {
        corgiUserRecommendMapper.addGroupCoordinate(userId, group);
    }

    @Override
    public void updateGroupCor(String userId, String group, Double weight) {
        corgiUserRecommendMapper.updateGroupCoordinate(userId, group, weight);
    }

    @Override
    public HashMap<String, Double> getGroupCor(String userId) {
        HashMap<String, Double> result = new HashMap<>();
        if ("all".equals(userId)) {
            List<UserDetail> cors = corgiUserRecommendMapper.getTotalGroupCoordinate(userId);
            for (UserDetail cor : cors) {
                result.put(cor.getGroup(), cor.getMatch());
            }
            return result;
        }
        List<UserDetail> cors = corgiUserRecommendMapper.getGroupCoordinate(userId);
        for (UserDetail cor : cors) {
            result.put(cor.getGroup(), cor.getMatch());
        }
        return result;
    }

    @Override
    public void addPreferCor(String userId, String group) {
        corgiUserRecommendMapper.addPreferCoordinate(userId, group);
    }

    @Override
    public void updatePreferCor(String userId, String group, Double weight) {
        corgiUserRecommendMapper.updatePreferCoordinate(userId, group, weight);
    }

    @Override
    public HashMap<String, Double> getPreferCor(String userId) {
        HashMap<String, Double> result = new HashMap<>();
        if ("all".equals(userId)) {
            List<UserDetail> cors = corgiUserRecommendMapper.getTotalGroupCoordinate(userId);
            for (UserDetail cor : cors) {
                result.put(cor.getGroup(), cor.getMatch());
            }
            return result;
        }

        List<UserDetail> cors = corgiUserRecommendMapper.getPreferCoordinate(userId);
        for (UserDetail cor : cors) {
            result.put(cor.getGroup(), cor.getMatch());
        }
        return result;
    }

    @Override
    public void clearPreferCor(String param) {
        corgiUserRecommendMapper.clearPreferGroupCoordinate();
    }

    @Override
    public Double getGroupWeight(Integer limit, String group) {
        if (limit == 0) {
            return corgiUserRecommendMapper.countTotalGroup() * 1.0;
        }
        String having = "";
        if (!groupOrder.get(0).equals(group)) {
            having = "having";
            for (String key : groupOrder) {
                if (key.equals(group)) {
                    break;
                }
                Double weight = Double.valueOf(redisTemplate.opsForHash().get("group_weight", key).toString());
                String havingGroup = " sum(if(`group` = '" + key + "',weight,0))/sum(weight) <= " + weight;
                if (!groupOrder.get(0).equals(key)) {
                    havingGroup = " and" + havingGroup;
                }
                having += havingGroup;
            }
        }
        Double result = corgiUserRecommendMapper.getGroupWeight(limit, group, having, "desc");
        if (result == null) {
            result = corgiUserRecommendMapper.getGroupWeight(0, group, having, "asc");
        }
        return result;
    }
}
