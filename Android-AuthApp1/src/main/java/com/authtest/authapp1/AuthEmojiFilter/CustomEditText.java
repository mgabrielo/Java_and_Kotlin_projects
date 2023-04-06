package com.authtest.authapp1.AuthEmojiFilter;

import android.content.Context;
import android.text.InputFilter;

import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;


import com.authtest.authapp1.R;

import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class CustomEditText extends AppCompatEditText {


    public CustomEditText(Context context) {
        super(context);


    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void init(AppCompatEditText edt) {
        edt.setFilters(new InputFilter[]{new EmojiIncludeFilter(), new InputFilter.LengthFilter(32)});

    }

    private class EmojiIncludeFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned spanned, int i2, int i3) {
            for (int a = start; a < end; a++) {
                int type = Character.getType(source.charAt(a));
                System.out.println(type);
                String blockSet3 = "★☆○●□■♤♠♡♥◇◆♧♣~°>|+©®×÷=™✓√∆℅∆≤≥<±≠≡№µ∞αβꭥ";
                String blockSet4 = "▪︎";
                String blockSet5 = "♥︎";
                String blockSet6 = "♣︎";
                String blockSet7 = "♠︎";

                if(source.length() == 0){
                    Toast.makeText(getContext(), "please enter emoji", Toast.LENGTH_SHORT).show();
                }
                if(blockSet3.contains(""+source) || blockSet4.contains(""+source) || blockSet5.contains(""+source) || blockSet6.contains(""+source) || blockSet7.contains(""+source)){
                    return "";
                } else if (type == 28){
                    return source;
                }else if (type == 19){
                    return source;
                }else if (type == 25){
                    //Toast.makeText(getContext(), "choose another emoji", Toast.LENGTH_SHORT).show();
                    return source;
                }else if (type == 6 ){
                    Toast.makeText(getContext(), "choose another emoji", Toast.LENGTH_SHORT).show();
                    return "";
                } else if (type != Character.SURROGATE && type != Character.OTHER_SYMBOL && type != Character.NON_SPACING_MARK ) {
                    // Other then emoji value will be blank
                   Toast.makeText(getContext(), "Only emojis allowed", Toast.LENGTH_SHORT).show();
                    return "";
                }


            }

            return source;

        }
    }





}
