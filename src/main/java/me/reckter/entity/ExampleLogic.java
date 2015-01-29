package me.reckter.entity;

import com.oracle.tools.packager.Log;
import me.reckter.entity.events.BeginTickEvent;
import me.reckter.entity.events.EndTickEvent;
import me.reckter.entity.logic.Entities;
import me.reckter.entity.logic.OnEvent;
import me.reckter.entity.logic.Logic;

/**
 * Created by hannes on 27.01.15.
 */
@Logic
public class ExampleLogic {

    @OnEvent()
    public void beginTick(BeginTickEvent event) {
        System.out.println("beginTick");
    }


    @OnEvent()
    public void endTick(EndTickEvent event) {
        System.out.println("endTick");
    }
}
