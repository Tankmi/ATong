package com.jemer.atong.net;

import io.reactivex.disposables.CompositeDisposable;

public  class ClearDisposable {
    public static CompositeDisposable compositeDisposable;
   public static ClearDisposable mClearDisposable;

 public static ClearDisposable getInstance(){
     synchronized (ClearDisposable.class){
         if (mClearDisposable == null){
             mClearDisposable = new ClearDisposable();
             compositeDisposable = new CompositeDisposable();
         }
     }
  return mClearDisposable;
 }


    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    public void clearNet(){
        compositeDisposable.clear();
    }
}
