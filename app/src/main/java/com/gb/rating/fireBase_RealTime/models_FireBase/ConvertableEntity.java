package com.gb.rating.fireBase_RealTime.models_FireBase;

public interface  ConvertableEntity <Entity, FB_Entity>{

    Entity convertToModelEntity();
    //FB_Entity convertFromModelEntity(Entity entity);
}
