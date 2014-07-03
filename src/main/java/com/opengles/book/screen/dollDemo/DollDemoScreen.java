package com.opengles.book.screen.dollDemo;

import com.opengles.book.framework.Game;
import com.opengles.book.screen.FrameBufferScreen;

import java.util.List;

/**
 *  人偶场景
 * Created by davidleen29   qq:67320337
 * on 2014-7-3.
 */
public class DollDemoScreen extends FrameBufferScreen{

    private Doll doll;
    private RigidObjObject[]  rigidObjObjects;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void pause() {
        super.pause();
        for (RigidObjObject objObject:rigidObjObjects)
        {
            objObject.unBind();
        }
    }

    @Override
    public void resume() {
        super.resume();
        for (RigidObjObject objObject:rigidObjObjects)
        {
            objObject.bind();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public DollDemoScreen(Game game) {
        super(game);
        rigidObjObjects=new RigidObjObject[Doll.BodyPartIndex.BODYPART_COUNT.ordinal()] ;

        doll=new Doll(rigidObjObjects);



    }


    public void initBodyForDraws(){
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_HEAD.ordinal()]=loadedModels[0];
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_SPINE.ordinal()]=loadedModels[1];
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_PELVIS.ordinal()]=loadedModels[2];
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_RIGHT_UPPER_ARM.ordinal()]=loadedModels[3];
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_LEFT_UPPER_ARM.ordinal()]=loadedModels[3].clone();
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_LEFT_LOWER_ARM.ordinal()]=loadedModels[4];
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_RIGHT_LOWER_ARM.ordinal()]=loadedModels[4].clone();
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_RIGHT_UPPER_LEG.ordinal()]=loadedModels[5];
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_LEFT_UPPER_LEG.ordinal()]=loadedModels[5].clone();
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_RIGHT_LOWER_LEG.ordinal()]=loadedModels[6];
        rigidObjObjects[Doll.BodyPartIndex.BODYPART_LEFT_LOWER_LEG.ordinal()]=loadedModels[6].clone();
//        for(int i=0;i<rigidObjObjects.length;i++){
//            lovnList.add(bodyForDraws[i]);
//        }
    }

    @Override
    protected void onPresent(float deltaData) {

    }
}
