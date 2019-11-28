package com.weedeo.user.ui.filter;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.weedeo.user.R;
import com.weedeo.user.model.CategoryResponseModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created By Athul on 14-11-2019.
 */
public class CategoryViewHolder extends ParentViewHolder {
    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;
   // private boolean isExpanded;

    //@BindView(R.id.category_text_view)
    TextView categoryTextView;

   // @BindView(R.id.iv_arrow_expand)
    ImageView arrowView;


    public CategoryViewHolder(View itemView) {
        super(itemView);
        //ButterKnife.bind(this,itemView);
        categoryTextView = itemView.findViewById(R.id.category_text_view);
        arrowView = itemView.findViewById(R.id.iv_arrow_expand);
    }

    /*@OnClick(R.id.iv_arrow_expand)
    public void onArrowClick(){
        if (isExpanded) {
            collapseView();
        } else {
            expandView();
        }
    }*/

    public void bind(CategoryResponseModel.DataBean category) {
        if (category.getSubCategories().size()==0)
            arrowView.setVisibility(View.GONE);
        else
            arrowView.setVisibility(View.VISIBLE);
        categoryTextView.setText(category.getName());
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
       // isExpanded = expanded;
        if (expanded) {
            arrowView.setRotation(ROTATED_POSITION);
        } else {
            arrowView.setRotation(INITIAL_POSITION);
        }

    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);

        RotateAnimation rotateAnimation;
        if (expanded) { // rotate clockwise
            rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        } else { // rotate counterclockwise
            rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        }

        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);
        arrowView.startAnimation(rotateAnimation);

    }
}

