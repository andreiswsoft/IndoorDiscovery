package ua.com.sweetsoft.indoordiscovery.fragment.graph;

import android.os.AsyncTask;

import java.util.List;

import ua.com.sweetsoft.indoordiscovery.fragment.grid.RecyclerViewData;

public class RefreshAsyncTask extends AsyncTask<Void, Void, List<SerieData>>
{
    private FragmentGraph m_fragment;

    public RefreshAsyncTask(FragmentGraph fragment)
    {
        m_fragment = fragment;
    }

    @Override
    protected List<SerieData> doInBackground(Void... params)
    {
        return SerieData.getList();
    }

    @Override
    protected void onPostExecute(List<SerieData> dataList)
    {
        m_fragment.endRefresh(dataList);
    }
}
