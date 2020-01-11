package com.gb.rating.fireBase.models_FireBase;

public interface  ConvertableEntity <Entity, FB_Entity>{

    Entity convertToModelEntity();
    //FB_Entity convertFromModelEntity(Entity entity);
}
