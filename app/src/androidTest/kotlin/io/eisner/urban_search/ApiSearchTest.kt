package io.eisner.urban_search

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import io.eisner.urban_search.data.Repository
import io.eisner.urban_search.data.Source
import io.eisner.urban_search.data.model.Track
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class ApiSearchTest : KoinTest {

    @get:Rule
    var activity: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java, false, false)
    private val mockRepository = mockk<Repository>()

    @Before
    fun setup() {
        loadKoinModules(module(override = true) {
            factory { mockRepository }
        })
    }

    @Test
    fun searchTest() {
        runBlocking {
            coEvery { mockRepository.searchFor(any(), any()) } returns flow {
                emit(
                    Source.Remote to listOf(
                        Track("Something", "Something", 10, "", emptyList())
                    )
                )
            }
            activity.launchActivity(null)
            onView(withId(R.id.search_button)).perform(click())
            onView(withId(R.id.search_src_text)).perform(typeText("Something\n"))

            onView(withId(R.id.recycler))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            onView(RecyclerViewMatcher(R.id.recycler).atPosition(0))
                .check(matches(hasDescendant(withText("Something - Something"))))
        }
    }

    @Test
    fun searchLoadingTest() {
        runBlocking {
            coEvery { mockRepository.searchFor(any(), any()) } returns flow {
                emit(Source.Local to listOf())
                delay(2000)
            }
            activity.launchActivity(null)
            onView(withId(R.id.search_button)).perform(click())
            onView(withId(R.id.search_src_text)).perform(typeText("Something\n"))
            delay(2000)
            onView(withId(R.id.loading)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        }
    }
}