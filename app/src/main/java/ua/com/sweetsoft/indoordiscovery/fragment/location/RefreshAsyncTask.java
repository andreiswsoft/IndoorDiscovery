package ua.com.sweetsoft.indoordiscovery.fragment.location;

import android.os.AsyncTask;

import java.util.List;

public class RefreshAsyncTask extends AsyncTask<Void, Void, List<ua.com.sweetsoft.indoordiscovery.fragment.location.RecyclerViewData>>
{
    private ua.com.sweetsoft.indoordiscovery.fragment.location.RecyclerViewAdapter m_adapter;

    public RefreshAsyncTask(ua.com.sweetsoft.indoordiscovery.fragment.location.RecyclerViewAdapter adapter)
    {
        m_adapter = adapter;
    }

    @Override
    protected List<ua.com.sweetsoft.indoordiscovery.fragment.location.RecyclerViewData> doInBackground(Void... params)
    {
        return ua.com.sweetsoft.indoordiscovery.fragment.location.RecyclerViewData.getList();
    }

    @Override
    protected void onPostExecute(List<RecyclerViewData> viewDataList)
    {
        m_adapter.endRefresh(viewDataList);
    }
}
