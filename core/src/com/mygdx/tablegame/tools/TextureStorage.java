package com.mygdx.tablegame.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

import java.util.ArrayList;
//класс, используемый для хранения текстур и их быстрой смены
//#TODO заменить на Map с статическим массивом строк(названий текстур)
public class TextureStorage {
    public static TextureAttribute[][] textures3d = new TextureAttribute[16][2];
    public static Texture[][] textures2d = new Texture[16][2];
    public static Texture[][] textures_UI=new Texture[2][];

    public TextureStorage() {
        ArrayList<TextureAttribute> temp3d = new ArrayList<>();
        ArrayList<Texture> temp2d = new ArrayList<>();
        TextureAttribute simple3D_texture1 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("card_texture1.png"));
        temp3d.add(simple3D_texture1);
        TextureAttribute selected3D_texture1 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("card_texture1.png"));
        temp3d.add(selected3D_texture1);
        Texture simple_2Dtexture1 = new Texture(Gdx.files.internal("card_texture.PNG"));
        temp2d.add(simple_2Dtexture1);
        Texture selected_2Dtexture1 = new Texture(Gdx.files.internal("card_texture_selected.PNG"));
        temp2d.add(selected_2Dtexture1);
        TextureAttribute simple3D_texture2 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("pshick3d.png"));
        temp3d.add(simple3D_texture2);
        TextureAttribute selected3D_texture2 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("pshick3d_selected.png"));
        temp3d.add(selected3D_texture2);
        Texture simple_2Dtexture2 = new Texture(Gdx.files.internal("pshick.png"));
        temp2d.add(simple_2Dtexture2);
        Texture selected_2Dtexture2 = new Texture(Gdx.files.internal("pshick_selected.png"));
        temp2d.add(selected_2Dtexture2);
        TextureAttribute simple3D_texture3 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("znak3D.png"));
        temp3d.add(simple3D_texture3);
        TextureAttribute selected3D_texture3 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("znak3D_selected.png"));
        temp3d.add(selected3D_texture3);
        Texture simple_2Dtexture3 = new Texture(Gdx.files.internal("znak.png"));
        temp2d.add(simple_2Dtexture3);
        Texture selected_2Dtexture3 = new Texture(Gdx.files.internal("znak_selected.png"));
        temp2d.add(selected_2Dtexture3);
        TextureAttribute simple3D_texture4 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("abrakadabrador3d.png"));
        temp3d.add(simple3D_texture4);
        TextureAttribute selected3D_texture4 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("abrakadabrador3d_selected.png"));
        temp3d.add(selected3D_texture4);
        Texture simple_2Dtexture4 = new Texture(Gdx.files.internal("abrakadabrador.png"));
        temp2d.add(simple_2Dtexture4);
        Texture selected_2Dtexture4 = new Texture(Gdx.files.internal("abrakadabrador_selected.png"));
        temp2d.add(selected_2Dtexture4);
        TextureAttribute simple3D_texture5 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("fireball3d.png"));
        temp3d.add(simple3D_texture5);
        TextureAttribute selected3D_texture5 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("fireball3d_selected.png"));
        temp3d.add(selected3D_texture5);
        Texture simple_2Dtexture5 = new Texture(Gdx.files.internal("fireball.png"));
        temp2d.add(simple_2Dtexture5);
        Texture selected_2Dtexture5 = new Texture(Gdx.files.internal("fireball_selected.png"));
        temp2d.add(selected_2Dtexture5);
        TextureAttribute simple3D_texture6 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("Amulet3d.png"));
        temp3d.add(simple3D_texture6);
        TextureAttribute selected3D_texture6 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("Amulet3d_selected.png"));
        temp3d.add(selected3D_texture6);
        Texture simple_2Dtexture6 = new Texture(Gdx.files.internal("Amulet.png"));
        temp2d.add(simple_2Dtexture6);
        Texture selected_2Dtexture6 = new Texture(Gdx.files.internal("Amulet_selected.png"));
        temp2d.add(selected_2Dtexture6);
        TextureAttribute simple3D_texture7 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("crown3d.png"));
        temp3d.add(simple3D_texture7);
        TextureAttribute selected3D_texture7 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("crown3d_selected.png"));
        temp3d.add(selected3D_texture7);
        Texture simple_2Dtexture7 = new Texture(Gdx.files.internal("crown.png"));
        temp2d.add(simple_2Dtexture7);
        Texture selected_2Dtexture7 = new Texture(Gdx.files.internal("crown_selected.png"));
        temp2d.add(selected_2Dtexture7);
        TextureAttribute simple3D_texture8 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("princess3d.png"));
        temp3d.add(simple3D_texture8);
        TextureAttribute selected3D_texture8 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("princess3d_selected.png"));
        temp3d.add(selected3D_texture8);
        Texture simple_2Dtexture8 = new Texture(Gdx.files.internal("princess.png"));
        temp2d.add(simple_2Dtexture8);
        Texture selected_2Dtexture8 = new Texture(Gdx.files.internal("princess_selected.png"));
        temp2d.add(selected_2Dtexture8);
        TextureAttribute simple3D_texture9 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("smallknight3d.png"));
        temp3d.add(simple3D_texture9);
        TextureAttribute selected3D_texture9 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("smallknight3d_selected.png"));
        temp3d.add(selected3D_texture9);
        Texture simple_2Dtexture9 = new Texture(Gdx.files.internal("smallknight.png"));
        temp2d.add(simple_2Dtexture9);
        Texture selected_2Dtexture9= new Texture(Gdx.files.internal("smallknight_selected.png"));
        temp2d.add(selected_2Dtexture9);
        TextureAttribute simple3D_texture10 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("solntselikiy3d.png"));
        temp3d.add(simple3D_texture10);
        TextureAttribute selected3D_texture10 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("solntselikiy3d_selected.png"));
        temp3d.add(selected3D_texture10);
        Texture simple_2Dtexture10 = new Texture(Gdx.files.internal("solntselikiy.png"));
        temp2d.add(simple_2Dtexture10);
        Texture selected_2Dtexture10 = new Texture(Gdx.files.internal("solntselikiy_selected.png"));
        temp2d.add(selected_2Dtexture10);
        TextureAttribute simple3D_texture11 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("spellbook3d.png"));
        temp3d.add(simple3D_texture11);
        TextureAttribute selected3D_texture11 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("spellbook3d_selected.png"));
        temp3d.add(selected3D_texture11);
        Texture simple_2Dtexture11 = new Texture(Gdx.files.internal("spellbook.png"));
        temp2d.add(simple_2Dtexture11);
        Texture selected_2Dtexture11 = new Texture(Gdx.files.internal("spellbook_selected.png"));
        temp2d.add(selected_2Dtexture11);
        TextureAttribute simple3D_texture12 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("stranger3d.png"));
        temp3d.add(simple3D_texture12);
        TextureAttribute selected3D_texture12 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("stranger3d_selected.png"));
        temp3d.add(selected3D_texture12);
        Texture simple_2Dtexture12 = new Texture(Gdx.files.internal("stranger.png"));
        temp2d.add(simple_2Dtexture12);
        Texture selected_2Dtexture12 = new Texture(Gdx.files.internal("stranger_selected.png"));
        temp2d.add(selected_2Dtexture12);
        TextureAttribute simple3D_texture13 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("summon3d.png"));
        temp3d.add(simple3D_texture13);
        TextureAttribute selected3D_texture13 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("summon3d_selected.png"));
        temp3d.add(selected3D_texture13);
        Texture simple_2Dtexture13 = new Texture(Gdx.files.internal("summon.png"));
        temp2d.add(simple_2Dtexture13);
        Texture selected_2Dtexture13 = new Texture(Gdx.files.internal("summon_selected.png"));
        temp2d.add(selected_2Dtexture13);
        TextureAttribute simple3D_texture14 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("twinbranches3d.png"));
        temp3d.add(simple3D_texture14);
        TextureAttribute selected3D_texture14 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("twinbranches3d_selected.png"));
        temp3d.add(selected3D_texture14);
        Texture simple_2Dtexture14= new Texture(Gdx.files.internal("twinbranches.png"));
        temp2d.add(simple_2Dtexture14);
        Texture selected_2Dtexture14 = new Texture(Gdx.files.internal("twinbranches_selected.png"));
        temp2d.add(selected_2Dtexture14);
        TextureAttribute simple3D_texture15 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("vampire3d.png"));
        temp3d.add(simple3D_texture15);
        TextureAttribute selected3D_texture15 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("vampire3d_selected.png"));
        temp3d.add(selected3D_texture15);
        Texture simple_2Dtexture15 = new Texture(Gdx.files.internal("vampire.png"));
        temp2d.add(simple_2Dtexture15);
        Texture selected_2Dtexture15 = new Texture(Gdx.files.internal("vampire_selected.png"));
        temp2d.add(selected_2Dtexture15);
        TextureAttribute simple3D_texture16 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("zdorovyak3d.png"));
        temp3d.add(simple3D_texture16);
        TextureAttribute selected3D_texture16 = new TextureAttribute(TextureAttribute.Diffuse, new Texture("zdorovyak3d_selected.png"));
        temp3d.add(selected3D_texture16);
        Texture simple_2Dtexture16 = new Texture(Gdx.files.internal("zdorovyak.png"));
        temp2d.add(simple_2Dtexture16);
        Texture selected_2Dtexture16 = new Texture(Gdx.files.internal("zdorovyak_selected.png"));
        temp2d.add(selected_2Dtexture16);

        Texture heart_UI = new Texture(Gdx.files.internal("heart.png"));
        Texture half_heart_UI = new Texture(Gdx.files.internal("half.png"));
        Texture armor=new Texture(Gdx.files.internal("shield.png"));
        for (int i = 0; i < textures3d.length; i++) {
            for (int j = 0; j < textures3d[i].length; j++) {
                textures3d[i][j] = temp3d.get(0);
                temp3d.remove(0);
            }
        }
        for (int i = 0; i < textures2d.length; i++) {
            for (int j = 0; j < textures2d[i].length; j++) {
                textures2d[i][j] = temp2d.get(0);
                temp2d.remove(0);
            }
        }
        textures_UI[0] = new Texture[2];
        textures_UI[0][0]= heart_UI;
        textures_UI[0][1]= half_heart_UI;
        textures_UI[1]=new Texture[1];
        textures_UI[1][0]=armor;
    }
}
