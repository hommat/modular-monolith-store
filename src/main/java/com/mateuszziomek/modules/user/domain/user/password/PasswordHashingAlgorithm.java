package com.mateuszziomek.modules.user.domain.user.password;

public interface PasswordHashingAlgorithm {
    HashedPassword hash(PlainPassword plainPassword);
}
