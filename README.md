# Async
This library is intended to ease the `Async` typical problem. Using this library will reduce the number lines required to do Android `AsyncTask`.

# Sample Usage
Given that `WebDownloader.java` is a class that downloads the whole internet (what!?). Since it may take more than 5 minutes for the process to complete, we should do an Async call so that it will not block the UI - maybe. Using this library, the async patterns becomes:
``` java
// do static import for readibility
import static com.bingzer.android.Async.doAsync;

public class WebDownloader {
  /* The actual method that does the work */
  public String download(){
    // download the whole internet here
    // and returns a gigantic String!
  }
  
  /* Async call for download() */ 
  public void downloadAsync(Task<String> task){
    doAsync(task, new Delegate<String>(){
      public String invoke(){
        return download();
      }
    });
  }
}
```

To actually uses the async stuffs:
``` java
WebDownloader downloader = new WebDownloader();
downloader.downloadAsync(new Task<String>(){
  public void onCompleted(String result){
    // this line of code, will be called
    // when the whole Internet is downloaded
    Log.i(TAG, result);
  }
});
```
