/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.githubrepolist.ui.activities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
//import api.RepositoryRetriever
import com.raywenderlich.android.githubrepolist.R
import com.raywenderlich.android.githubrepolist.api.RepositoryRetriever
import com.raywenderlich.android.githubrepolist.ui.adapters.RepoListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    // Switch to AppTheme for displaying the activity
    setTheme(R.style.AppTheme)

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    repoList.layoutManager = LinearLayoutManager(this)

    if (isNetworkConnected()) {
      retrieveRepositories()
    } else {
      AlertDialog.Builder(this).setTitle("No Internet Connection")
              .setMessage("Please check your internet connection and try again")
              .setPositiveButton(android.R.string.ok) { _, _ -> }
              .setIcon(android.R.drawable.ic_dialog_alert).show()
    }
  }

  fun retrieveRepositories() {
    //1 Create a Coroutine scope using a job to be able to cancel when needed
    val mainActivityJob = Job()

    //2 Handle exceptions if any
    val errorHandler = CoroutineExceptionHandler { _, exception ->
      AlertDialog.Builder(this).setTitle("Error")
              .setMessage(exception.message)
              .setPositiveButton(android.R.string.ok) { _, _ -> }
              .setIcon(android.R.drawable.ic_dialog_alert).show()
    }

    //3 the Coroutine runs using the Main (UI) dispatcher
    val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
    coroutineScope.launch(errorHandler) {
      //4
      val resultList = RepositoryRetriever().getRepositories()
      repoList.adapter = RepoListAdapter(resultList)
    }
  }

  private fun isNetworkConnected(): Boolean {
    //1
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    //2
    val activeNetwork = connectivityManager.activeNetwork
    //3
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
    //4
    return networkCapabilities != null &&
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
  }

}
