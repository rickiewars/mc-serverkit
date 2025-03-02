package com.rwconnected.serverkit.http.Models;

import java.util.List;

public record Objective(String name, String criterion, String displayName, List<Score> scores) {
    // Constructor
}