package com.honeyboard.api.generation.service;

import com.honeyboard.api.user.model.User;
import com.honeyboard.api.generation.model.Generation;

import java.util.List;

public interface GenerationService {

    List<Generation> getAllGenerations();

    List<User> getUserByGeneration(int generationId);
}
