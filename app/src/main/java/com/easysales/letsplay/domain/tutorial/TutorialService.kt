package com.easysales.letsplay.domain.tutorial

import com.easysales.letsplay.data.dto.TutorialDto
import io.reactivex.Observable

interface TutorialService {
    fun getTutorials() : Observable<TutorialDto>
}