package org.ssutown.manna.AdjustDaySchedule;

/**
 * Created by YNH on 2017. 6. 1..
 */

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Raquib-ul-Alam Kanak on 1/3/16.
 * Website: http://alamkanak.github.io
 */
public interface MyJsonService {

    //https://api.myjson.com/bins/3nh0k
    @GET("/3nh0k")
    void listEvents(Callback<List<Event>> eventsCallback);

}