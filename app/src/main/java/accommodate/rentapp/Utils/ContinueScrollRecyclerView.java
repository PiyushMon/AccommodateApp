package accommodate.rentapp.Utils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

public abstract class ContinueScrollRecyclerView extends OnScrollListener {
    public static String TAG = "ContinueScrollRecyclerView";
    int firstVisibleItem;
    int totalItemCount;
    int visibleItemCount;
    private int current_page = 1;
    private boolean loading = true;
    private LinearLayoutManager mLinearLayoutManager;
    private int previousTotal = 0;
    private int visibleThreshold = 1;

    public ContinueScrollRecyclerView(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    public abstract void onLoadMore(int i);

    public void onScrolled(RecyclerView recyclerView, int i, int i2) {
        super.onScrolled(recyclerView, i, i2);
        this.visibleItemCount = recyclerView.getChildCount();
        this.totalItemCount = this.mLinearLayoutManager.getItemCount();
        int findFirstVisibleItemPosition = this.mLinearLayoutManager.findFirstVisibleItemPosition();
        this.firstVisibleItem = findFirstVisibleItemPosition;
        i = this.visibleItemCount;
        if (i == 0) {
            this.previousTotal = 0;
            this.loading = true;
        }
        if (this.loading) {
            int i3 = this.totalItemCount;
            if (i3 > this.previousTotal + 1) {
                this.loading = false;
                this.previousTotal = i3;
            }
        }
        if (!this.loading && this.totalItemCount - i <= findFirstVisibleItemPosition + this.visibleThreshold) {
            findFirstVisibleItemPosition = this.current_page + 1;
            this.current_page = findFirstVisibleItemPosition;
            onLoadMore(findFirstVisibleItemPosition);
            this.loading = true;
        }
    }
}
