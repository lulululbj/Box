package luyao.box.ui.file

import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_file.*
import luyao.box.R
import luyao.box.adapter.FileAdapter
import luyao.box.bean.BoxFile
import luyao.box.bean.IFile
import luyao.util.ktx.base.BaseVMActivity
import luyao.util.ktx.ext.toast
import java.io.File

/**
 * Created by luyao
 * on 2019/7/19 9:46
 */
class FileActivity : BaseVMActivity<FileViewModel>() {

    private lateinit var mMenu :Menu
    private var rootPath: String = "/"
    private var currentFile: File = File(rootPath)
    private val fileAdapter by lazy { FileAdapter() }
    override fun providerVMClass() = FileViewModel::class.java

    companion object {
        const val PATH = "root_path"
    }

    override fun getLayoutResId() = R.layout.activity_file

    override fun initView() {
        setSupportActionBar(mToolbar)
        rootPath = intent.getStringExtra(PATH) ?: "/sdcard"

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        mToolbar.setNavigationOnClickListener { onBackPressed() }
        mToolbar.title = "文件管理"

        initRefreshLayout()
        initRecycleView()
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

    private fun refresh() {
        mViewModel.getFileListAsync(rootPath)
    }

    private fun showPopMenu(view: View, file: IFile) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu_file_pop, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_cut -> {
                        mMenu.findItem(R.id.menu_file_paste).isVisible=true
                }
                R.id.menu_copy -> {
                }
                R.id.menu_delete -> {
                }
                R.id.menu_rename -> {
                }
                R.id.menu_encrypt -> {
                }
                R.id.menu_property -> {
                }
            }
            true
        }
        popupMenu.show()
    }

    override fun onBackPressed() {
        if (currentFile.parentFile == null)
            super.onBackPressed()
        else {
            rootPath = currentFile.parent
            refresh()
        }
    }

    override fun startObserve() {
        mViewModel.fileListData.observe(this, Observer { it?.let { refreshFileData(it) } })
    }

    private fun refreshFileData(fileList: List<BoxFile>) {
        fileAdapter.setNewData(fileList)
        fileRefreshLayout.isRefreshing = false
        currentPathTv.text = rootPath
        currentFile = File(rootPath)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_file, menu)
        menu?.let { mMenu=it }
        return true
    }
}