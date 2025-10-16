package com.terracetech.tims.mobile.common.manager;

public class PageManager {
	private int total;
    private int pageSize;
    private int windowSize;
    private int page;

	public void initParameter(int total, int pageSize, int windowSize) {
        this.total = total;
        this.pageSize = pageSize;
        this.windowSize = windowSize;
	}

    public int getNextPage() {
        int npages = (int) Math.ceil((double) total / pageSize);

        if (page < npages) {
            return page + 1;
        }
        else {
            return page;
        }
    }

    public int getNextWindow() {
        int npages = (int) Math.ceil((double) total / pageSize);
        if(npages >= (page + windowSize)){
            return page + windowSize;
        }
        else {
            return -1;
        }
    }

    public int getPage() {
        return page;
    }

    public int getPageCount() {
        return (int) Math.ceil((double) total / pageSize);
    }

    public int[] getPages() {
        int npages = (int) Math.ceil((double) total / pageSize);
        int off = (page - 1) / windowSize * windowSize;
        int len = Math.min(Math.min(windowSize, npages), npages - off);
        int[] pages = new int[len];

        for (int i = 0; i < pages.length; i++) {
            pages[i] = off + i + 1;
        }

        return pages;
    }

    public int getPrevPage() {
        if (page > 1) {
            return page - 1;
        }
        else {
            return page;
        }
    }

    public int getPrevWindow() {
        if (page > windowSize) {
            return page - windowSize;
        }
        else {
            return -1;
        }
    }

    public boolean isFirstPage() {
        return page == 1;
    }

    public boolean isFirstWindow() {
        return (page - windowSize) <= 0;
    }

    public boolean isLastPage() {
        int npages = (int) Math.ceil((double) total / pageSize);

        return npages <= page;
    }

    public boolean isLastWindow() {
        int npages = (int) Math.ceil((double) total / pageSize);        

        return npages < (page + windowSize);
    }

    public void nextPage() {
        int npages = (int) Math.ceil((double) total / pageSize);

        if (page < npages) {
            page++;
        }
    }

    public void prevPage() {
        if (page > 1) {
            page--;
        }
    }

    public void setPage(int page) {
        this.page = page;
    }
}
