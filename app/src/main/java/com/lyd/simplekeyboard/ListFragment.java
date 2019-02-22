package com.lyd.simplekeyboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lyd.keyboard.KeyboardAdapter;
import com.lyd.keyboard.KeyboardManage;
import com.lyd.keyboard.NumberKeyboardAdapter;

/**
 * @author lyd
 * @date 2019/2/19 0019 15:40
 * @desription
 */
public class ListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ListAdapter listAdapter = new ListAdapter(getActivity());
        recyclerView.setAdapter(listAdapter);
    }


    class ListAdapter extends RecyclerView.Adapter<ListHolder> {

        private KeyboardAdapter keyboardAdapter;
        private KeyboardManage keyboardManage;

        public ListAdapter(Activity activity) {
            keyboardAdapter = new NumberKeyboardAdapter(activity);
            keyboardManage = new KeyboardManage(activity, keyboardAdapter);
        }

        @NonNull
        @Override
        public ListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ListHolder(LayoutInflater.from(getContext()).inflate(R.layout.item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ListHolder viewHolder, int i) {
            viewHolder.editText.setHint("position" + i);
            if(i%3==0){
                return;
            }
            keyboardManage.add(viewHolder.editText);
        }

        @Override
        public int getItemCount() {
            return 50;
        }
    }

    class ListHolder extends RecyclerView.ViewHolder {

        private EditText editText;

        public ListHolder(@NonNull View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.item);
        }
    }
}
