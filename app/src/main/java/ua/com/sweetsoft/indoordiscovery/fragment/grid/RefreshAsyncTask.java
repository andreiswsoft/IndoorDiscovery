package ua.com.sweetsoft.indoordiscovery.fragment.grid;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class RefreshAsyncTask extends AsyncTask<Void, Void, List<RecyclerViewData>>
{
    private RecyclerViewAdapter m_adapter;

    public RefreshAsyncTask(RecyclerViewAdapter adapter)
    {
        m_adapter = adapter;
    }

    @Override
    protected List<RecyclerViewData> doInBackground(Void... params)
    {
        return RecyclerViewData.getList();
    }

    @Override
    protected void onPostExecute(List<RecyclerViewData> viewDataList)
    {
        m_adapter.endRefresh(viewDataList);
    }
}
