package com.easysales.letsplay

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.easysales.letsplay.data.db.Database
import com.easysales.letsplay.data.fake.FakeFactory
import com.easysales.letsplay.data.fake.FakeRepository
import com.easysales.letsplay.di.TestComponentManager
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito
import javax.inject.Inject


open class BaseTest {
    @Rule
    @JvmField var instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var componentManager: TestComponentManager
    protected lateinit var fakeRepository: FakeRepository
    protected lateinit var fakeFactory: FakeFactory

    @Inject lateinit var database: Database

    @Before
    open fun setup() {
        val scheduler = TestScheduler()
        RxJavaPlugins.setIoSchedulerHandler { scheduler }
        RxJavaPlugins.setComputationSchedulerHandler { scheduler }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler }

        componentManager = TestComponentManager()
        componentManager.appComponent.inject(this)

        fakeRepository = FakeRepository(database)
        fakeFactory = FakeFactory()
    }

    @After
    open fun cleanup() {
        database.clearAllTables()
    }

    protected fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}