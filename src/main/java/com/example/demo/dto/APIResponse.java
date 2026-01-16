package com.example.demo.dto;

import java.time.LocalDateTime;

public record APIResponse<T> (
    int status,
    String message,
    LocalDateTime timestamp,
    T data
){
}


