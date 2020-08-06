package chapter.android.aweme.ss.com.homework;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import chapter.android.aweme.ss.com.homework.model.Message;
import chapter.android.aweme.ss.com.homework.widget.CircleImageView;

public class MyAdapter  extends RecyclerView.Adapter<MyAdapter.NumberViewHolder>{
    private static final String TAG = "GreenAdapter";

    private int mNumberItems;

    private final ListItemClickListener mOnClickListener;

    private static int viewHolderCount;

    private List<Message> list;

    public MyAdapter(int numListItems, ListItemClickListener listener, List<Message> tempList) {
        mNumberItems = numListItems;
        mOnClickListener = listener;
        viewHolderCount = 0;
        list = tempList;
    }



    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.im_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);




        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: " + viewHolderCount);
        viewHolderCount++;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder numberViewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: #" + position);
        numberViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    public class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView title;
        private final TextView description;
        private final TextView time;
        private final ImageView imageView;
        private final CircleImageView circleImageView;


        public NumberViewHolder(@NonNull View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.tv_description);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            imageView = (ImageView) itemView.findViewById(R.id.robot_notice);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.iv_avatar);

            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
           title.setText(list.get(position).getTitle());
           time.setText(list.get(position).getTime());
           description.setText(list.get(position).getDescription());
           if(list.get(position).isOfficial()){
              imageView.setVisibility(View.VISIBLE);
           }
           switch (list.get(position).getIcon()){
                case "TYPE_USER":
                    circleImageView.setImageResource(R.drawable.icon_girl);
                    break;
                case "TYPE_GAME":
                    circleImageView.setImageResource(R.drawable.icon_micro_game_comment);
                    break;
                case "TYPE_SYSTEM":
                    circleImageView.setImageResource(R.drawable.session_system_notice);
                    break;
                case "TYPE_ROBOT":
                    circleImageView.setImageResource(R.drawable.session_robot);
                    break;
                case "TYPE_STRANGER":
                    circleImageView.setImageResource(R.drawable.session_stranger);
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            if (mOnClickListener != null) {
                mOnClickListener.onListItemClick(clickedPosition);
            }
        }
    }
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

}
