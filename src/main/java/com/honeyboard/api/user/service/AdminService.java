package com.honeyboard.api.user.service;


import com.honeyboard.api.user.model.User;

import java.util.List;

public interface AdminService {

    List<User> getUserByGeneration(int generationId);
}
