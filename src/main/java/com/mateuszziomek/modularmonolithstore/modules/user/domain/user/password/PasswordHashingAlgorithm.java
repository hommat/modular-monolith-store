package com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password;

public interface PasswordHashingAlgorithm {
    HashedPassword hash(PlainPassword plainPassword);
}
