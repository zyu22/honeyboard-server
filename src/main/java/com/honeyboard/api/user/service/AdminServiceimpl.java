package com.honeyboard.api.user.service;

import com.honeyboard.api.user.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceimpl implements AdminService {
    @Override
    public List<User> getUserByGeneration(int generationId) {
        return List.of();
    }
}
