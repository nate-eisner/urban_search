package io.eisner.urban_search

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.eisner.urban_search.data.Repository
import io.eisner.urban_search.data.model.UrbanDefinition
import io.reactivex.Flowable
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
    private val mockRepository = mock<Repository>()

    @Before
    fun setup() {
        loadKoinModules(module(override = true) {
            factory { mockRepository }
        })
    }

    @Test
    fun searchTest() {
        whenever(mockRepository.searchFor(any(), any())).thenReturn(
            Flowable.just(
                listOf(
                    UrbanDefinition(0, "Something something", "test", 1, 0)
                )
            )
        )
        activity.launchActivity(null)
        onView(withId(R.id.search_button)).perform(click())
        onView(withId(R.id.search_src_text)).perform(typeText("text\n"))

        onView(withId(R.id.recycler))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(RecyclerViewMatcher(R.id.recycler).atPosition(0))
            .check(matches(hasDescendant(withText("Something something"))))
    }
}