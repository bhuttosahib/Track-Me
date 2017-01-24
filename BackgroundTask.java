package symblink.com.track_me;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Menu;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by Meraj Ahmed on 10/6/2016.
 */
public class BackgroundTask extends AsyncTask<String, Void, String> {
        Context ctx;

    String logid,log;
    AlertDialog alertDialog;
    BackgroundTask(Context ctx){
this.ctx=ctx;

    }
    @Override
protected void onPreExecute(){

        alertDialog=new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Login Information");
    }
    @Override
    protected String doInBackground(String... params) {
        String method=params[0];
        String reg_url="http://10.0.2.2/appdb/register.php";
        String login_url="http://10.0.2.2/appdb/login.php";


        if (method.equals("registeration")){
            String stuname=params[1];
            String stuid=params[2];
            String stupass=params[3];
            String stuprg=params[4];
            String stucontact=params[5];
            String stuadd=params[6];
            String stufee=params[7];
            try {
                URL url=new URL(reg_url);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS=httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                 String data= URLEncoder.encode("stuname","UTF-8")+"="+ URLEncoder.encode(stuname,"UTF-8")+"&"
                         + URLEncoder.encode("stuid","UTF-8")+"="+ URLEncoder.encode(stuid,"UTF-8")+"&"+
                         URLEncoder.encode("stupass","UTF-8")+"="+ URLEncoder.encode(stupass,"UTF-8")+"&"+
                         URLEncoder.encode("stuprg","UTF-8")+"="+ URLEncoder.encode(stuprg,"UTF-8")+"&"
                         + URLEncoder.encode("stucontact","UTF-8")+"="+ URLEncoder.encode(stucontact,"UTF-8")+"&"
                         + URLEncoder.encode("stuadd","UTF-8")+"="+ URLEncoder.encode(stuadd,"UTF-8") +"&"
                         + URLEncoder.encode("stufee","UTF-8")+"="+ URLEncoder.encode(stufee,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS=httpURLConnection.getInputStream();
                IS.close();
                return  "Success";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if (method.equals("login")) {

            String logid = params[1];
            String logpass = params[2];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

          httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("logid", "UTF-8") + "=" + URLEncoder.encode(logid, "UTF-8") + "&" +
                        URLEncoder.encode("logpass", "UTF-8") + "=" + URLEncoder.encode(logpass, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;


                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


                return response;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
//

        return null;


    }
    @Override
    protected void  onProgressUpdate(Void... values){

        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(String result) {
        if(result == null)
        {
            alertDialog.setMessage("Please Fill In Login Information");
            // do what you want to do
        }

        if(result.equals("Success")){
            Toast.makeText(ctx,result, Toast.LENGTH_LONG).show();
            Intent i = new Intent(ctx,MainActivity.class);

            ctx.startActivity(i);

        }

    else if(result.contains("Login Successful")) // msg you get from success like "Login Success"
       {
          MainActivity mm=new MainActivity();

         Intent i = new Intent(ctx,Menu.class);

                ctx.startActivity(i);
            alertDialog.setMessage(result);
            alertDialog.show();

        }



        else
        {
            alertDialog.setMessage(result);
            alertDialog.show();
        }
    }

}
