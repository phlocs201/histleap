package tech.phlocs.histleap.async_task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetImageAsyncTask extends AsyncTask<Uri.Builder, Void, Bitmap> {
    private ImageView imageView;
    private RelativeLayout progressView;

    public GetImageAsyncTask(ImageView imageView, RelativeLayout progressView) {
        super();
        this.imageView = imageView;
        this.progressView = progressView;
    }

    @Override
    protected Bitmap doInBackground(Uri.Builder... builder) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        Bitmap bitmap = null;

        try {
            URL url = new URL(builder[0].toString());
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            inputStream = connection.getInputStream();

            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        // スポット画像を設定
        imageView.setImageBitmap(result);

        progressView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
//        public void onClickToggleSlider(View view) {
//            RelativeLayout inputArea = (RelativeLayout)findViewById(R.id.slider_area);
//            if (inputArea.getVisibility() == View.VISIBLE) {
//                inputArea.setVisibility(View.INVISIBLE);
//            } else {
//                inputArea.setVisibility(View.VISIBLE);
//            }
//        }
    }
}
