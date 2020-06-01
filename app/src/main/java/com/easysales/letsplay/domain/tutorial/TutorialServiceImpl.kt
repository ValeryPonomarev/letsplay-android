package com.easysales.letsplay.domain.tutorial

import com.easysales.letsplay.data.dto.TutorialDto
import io.reactivex.Observable

class TutorialServiceImpl : TutorialService {

    private class Tutorial(override val index: Int, override val url: String) : TutorialDto {
    }

    override fun getTutorials(): Observable<TutorialDto> {
        val result = ArrayList<Tutorial>()

        result.add(
            Tutorial(
                1,
                "http://forteapps.com/producto/tutorial/producto_tutorial_screen-1.png"
            )
        )
        result.add(
            Tutorial(
                1,
                "http://forteapps.com/producto/tutorial/producto_tutorial_screen-2.png"
            )
        )
        result.add(
            Tutorial(
                1,
                "http://forteapps.com/producto/tutorial/producto_tutorial_screen-3.png"
            )
        )
        result.add(
            Tutorial(
                1,
                "http://forteapps.com/producto/tutorial/producto_tutorial_screen-4.png"
            )
        )
        result.add(
            Tutorial(
                1,
                "http://forteapps.com/producto/tutorial/producto_tutorial_screen-5.png"
            )
        )
        result.add(
            Tutorial(
                1,
                "http://forteapps.com/producto/tutorial/producto_tutorial_screen-6.png"
            )
        )

        return Observable.fromIterable(result)
    }
}