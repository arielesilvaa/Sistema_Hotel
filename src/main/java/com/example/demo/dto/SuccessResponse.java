package com.example.demo.dto;

public record SuccessResponse<T>(
        T data
) {}
