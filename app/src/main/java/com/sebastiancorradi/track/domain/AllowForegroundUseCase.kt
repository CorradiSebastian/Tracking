package com.sebastiancorradi.track.domain

class AllowForegroundUseCase {

    operator fun invoke(): Boolean {

        //suspend operator fun invoke(): StartResult {
        /*val result = webService.login(username, password)

        return if (result.isSuccessful) {
            val user = result.body()

            LoginResult.Success(user)
        } else {
            LoginResult.Failure
        }*/
        return true
    }
}