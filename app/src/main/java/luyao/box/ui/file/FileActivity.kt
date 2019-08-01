package luyao.box.ui.file

import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import kotlinx.android.synthetic.main.activity_file.*
import luyao.box.R
import luyao.box.adapter.FileAdapter
import luyao.box.bean.IFile
import luyao.box.listFiles
import luyao.box.view.showCreateFileOrFolderDialog
import luyao.box.view.showPropertiesDialog
import luyao.util.ktx.base.BaseVMActivity
import luyao.util.ktx.ext.invisible
import luyao.util.ktx.ext.loge
import luyao.util.ktx.ext.toast
import luyao.util.ktx.ext.visible
import java.io.File
import java.io.FilenameFilter

/**
 * Created by luyao
 * on 2019/7/19 9:46
 */
class FileActivity : BaseVMActivity<FileViewModel>() {

    private var reserved = true // 是否保留源文件
    private var mMenu: Menu? = null
    private var rootPath: String = "/"
    private var currentFile: File = File(rootPath)
    private val fileAdapter by lazy { FileAdapter() }
    override fun providerVMClass() = FileViewModel::class.java

    private val selectFileList = ArrayList<IFile>()

    companion object {
        const val PATH = "root_path"
    }

    override fun getLayoutResId() = R.layout.activity_file

    override fun initView() {
        setSupportActionBar(mToolbar)
        rootPath = intent.getStringExtra(PATH) ?: Environment.getExternalStorageDirectory().path

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        mToolbar.setNavigationOnClickListener { onBackPressed() }
        mToolbar.title = "文件管理"

        initRefreshLayout()
        initRecycleView()
        initFabMenu()
    }

    override fun initData() {
        refresh()
    }

    private fun initRefreshLayout() {
        fileRefreshLayout.isRefreshing = true
        fileRefreshLayout.setOnRefreshListener { refresh() }
    }

    private fun initRecycleView() {
        fileRecycleView.run {
            isNestedScrollingEnabled = true
            layoutManager = LinearLayoutManager(this@FileActivity)
            adapter = fileAdapter
        }

        fileAdapter.run {

            setMenuClickListener { view, file ->
                showPopMenu(view, file)
            }

            setOnItemClickListener { _, _, position ->
                run {
                    val boxFile = fileAdapter.data[position]
                    if (boxFile.isDirectory()) {
                        rootPath = boxFile.filePath
                        refresh()
                    } else
                        boxFile.getMimeType()?.let {
                            toast(it)
                        }

                    val listFiles = boxFile.getFile().listFiles(true) { it.name.endsWith("txt") }
                    for (file in listFiles)
                        file.path.loge("box")

                }

                setOnItemLongClickListener { _, _, position ->
                    fileAdapter.let {
                        it.selectMode = !it.selectMode
                        it.notifyDataSetChanged()
                    }
                    true
                }
            }
        }
    }

    private fun initFabMenu() {
        fabMenu.inflate(R.menu.menu_fab)
        fabMenu.setOnActionSelectedListener { item ->
            when (item.id) {
                R.id.menu_add_file -> {
                    showCreateFileOrFolderDialog(this, false) {
                        mViewModel.createFile(currentFile,it)
                    }
                }
                R.id.menu_add_folder -> {
                    showCreateFileOrFolderDialog(this, true) {
                        mViewModel.createFolder(currentFile,it)
                    }
                }
            }
            false
        }
    }

    private fun refresh() {
        mViewModel.getFileListAsync(rootPath)
    }

    private fun showPopMenu(view: View, file: IFile) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu_file_pop, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_cut -> {
                    reserved = false
                    selectFileList.add(file)
                    mMenu?.let { it.findItem(R.id.menu_file_paste).isVisible = true }
                }
                R.id.menu_copy -> {
                    reserved = true
                    selectFileList.add(file)
                    mMenu?.let { it.findItem(R.id.menu_file_paste).isVisible = true }
                }
                R.id.menu_delete -> {
                    mViewModel.deleteAsync(file.getFile())
                }
                R.id.menu_rename -> {
                    showRenameDialog(file.getFile())
                }
                R.id.menu_encrypt -> {
                }
                R.id.menu_property -> {
                    showPropertiesDialog(this, file.getFile())
                }
            }
            true
        }
        popupMenu.show()
    }


    private fun paste() {
        mViewModel.pasteAsync(selectFileList, currentFile, reserved)
    }

    private fun refreshFileData(fileList: List<IFile>) {
        fileAdapter.setNewData(fileList)
        fileRefreshLayout.isRefreshing = false
        currentPathTv.text = rootPath
        currentFile = File(rootPath)
    }

    private fun showRenameDialog(file: File) {
        MaterialDialog(this).show {
            title(R.string.rename)
            input(hint = file.name, allowEmpty = false) { _, text ->
                mViewModel.renameFile(file, text.toString())
            }
            positiveButton(R.string.confirm)
            negativeButton(R.string.cancel)
        }
    }

    override fun startObserve() {
        mViewModel.fileListData.observe(this, Observer { it?.let { refreshFileData(it) } })

        mViewModel.mProgress.observe(this, Observer {
            it?.let {

                if (it == -1) {
                    fileProgressView.invisible()
                } else {
                    fileProgressView.visible()
                    fileProgressView.percent = it.toFloat()
                }
            }
        })

        mViewModel.mCurrentFileName.observe(this, Observer { it?.let { currentPathTv.text = it } })
        mViewModel.mRefreshTag.observe(this, Observer {
            it?.let {
                selectFileList.clear()
                refresh()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_file, menu)
        menu?.let { mMenu = it }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_file_paste -> {
                paste()
                mMenu?.let { it.findItem(R.id.menu_file_paste).isVisible = false }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (currentFile == Environment.getExternalStorageDirectory())
            super.onBackPressed()
        else {
            rootPath = currentFile.parent
            refresh()
        }
    }
}

