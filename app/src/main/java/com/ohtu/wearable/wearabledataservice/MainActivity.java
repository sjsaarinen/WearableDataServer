package com.ohtu.wearable.wearabledataservice;

import android.hardware.Sensor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.ohtu.wearable.wearabledataservice.web.HTMLPage;

import java.util.List;

public class MainActivity extends FragmentActivity implements SelectedSensorsInterface {
    ViewPager viewpager;

    List<Sensor> sensors;

    public void setSelectedSensors(List<Sensor> sensors){
        this.sensors = sensors;
    }

    public List<Sensor> getSelectedSensors(){
        return this.sensors;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wearablewebview_layout);
        HTMLPage htmlPage=new HTMLPage((android.widget.LinearLayout) findViewById(R.id.wearableWebView), "<html><head></head><body><span>Jotain tekstiä!</span><span>Jotain tekstiä!</span><button>Kokeillaan pitempää tekstiä</button></body></html>");
        htmlPage.renderHtmlPage(htmlPage);
    }

}
