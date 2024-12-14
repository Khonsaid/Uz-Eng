package uz.gita.latizx.uz_eng.presenter.ui.info

interface InfoContract {
    interface InfoViewModel{
        fun openPrevScreen()
    }
    interface InfoDirection{
        suspend fun navigateToBack()
    }
}