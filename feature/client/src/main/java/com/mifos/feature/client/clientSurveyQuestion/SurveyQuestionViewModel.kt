/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package com.mifos.feature.client.clientSurveyQuestion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mifos.core.common.utils.Constants
import com.mifos.core.data.repository.SurveyQuestionRepository
import com.mifos.core.objects.survey.Survey
import com.mifos.feature.client.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SurveyQuestionViewModel @Inject constructor(
    private val surveyQuestionRepository: SurveyQuestionRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val surveyId = savedStateHandle.getStateFlow(key = Constants.CLIENT_ID, initialValue = -1)
    private val _surveyQuestionUiState =
        MutableStateFlow<SurveyQuestionUiState>(SurveyQuestionUiState.ShowProgressbar)
    val surveyQuestionUiState: StateFlow<SurveyQuestionUiState> get() = _surveyQuestionUiState

    private lateinit var mSyncSurvey: Survey

    fun loadSurvey(surveyId: Int) {
        _surveyQuestionUiState.value = SurveyQuestionUiState.ShowProgressbar

        surveyQuestionRepository.getSurvey(surveyId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Subscriber<Survey>() {
                override fun onCompleted() {
                    _surveyQuestionUiState.value = SurveyQuestionUiState.ShowQuestions(ques = mSyncSurvey)
                }
                override fun onError(e: Throwable) {
                    _surveyQuestionUiState.value =
                        SurveyQuestionUiState.ShowFetchingError(R.string.feature_client_failed_to_fetch_survey_questions)
                }

                override fun onNext(survey: Survey) {
                    mSyncSurvey = survey
                }
            })
    }
}
