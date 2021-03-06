package com.azabost.simplemvvm.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.azabost.simplemvvm.R
import com.azabost.simplemvvm.di.ViewModelFactory
import com.azabost.simplemvvm.ui.BaseActivity
import com.azabost.simplemvvm.ui.repo.RepoActivity
import com.azabost.simplemvvm.utils.hide
import com.azabost.simplemvvm.utils.show
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var vmFactory: ViewModelFactory<MainViewModel>

    lateinit var vm: MainVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vm = vmFactory.get()

        vm.progress.observeOnMainThreadAndAutoDispose().subscribe {
            if (it) progress.show() else progress.hide()
        }

        vm.errors.observeOnMainThreadAndAutoDispose().subscribe {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

        vm.data.observeOnMainThreadAndAutoDispose().subscribe {
            val intent = Intent(this, RepoActivity::class.java).apply {
                putExtra(RepoActivity.REPO_RESPONSE_EXTRA, it)
            }
            startActivity(intent)
        }

        getRepoDataButton.setOnClickListener {
            vm.getRepoData(repoNameInput.text.toString())
        }
    }
}