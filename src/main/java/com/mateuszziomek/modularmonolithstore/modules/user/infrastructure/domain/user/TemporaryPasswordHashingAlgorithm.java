package com.mateuszziomek.modularmonolithstore.modules.user.infrastructure.domain.user;

import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.HashedPassword;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PasswordHashingAlgorithm;
import com.mateuszziomek.modularmonolithstore.modules.user.domain.user.password.PlainPassword;

public class TemporaryPasswordHashingAlgorithm implements PasswordHashingAlgorithm {
    @Override
    public HashedPassword hash(PlainPassword plainPassword) {
        return new HashedPassword("h_" + plainPassword.value() );
    }
}
