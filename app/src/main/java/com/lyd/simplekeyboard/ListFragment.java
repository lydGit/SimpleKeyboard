package com.lyd.simplekeyboard;

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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lyd.keyboard.KeyboardAdapter;
import com.lyd.keyboard.KeyboardManage;

/**
 * @author lyd
 * @date 2019/2/19 0019 15:40
 * @desription
 */
public class ListFragment extends Fragment {

    private KeyboardManage keyboardManage;
    private RecyclerView recyclerView;

    private KeyboardAdapter keyboardAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ListAdapter listAdapter = new ListAdapter();
        recyclerView.setAdapter(listAdapter);
        keyboardAdapter = new NumberKeyboardAdapter(getContext());
        keyboardManage = new KeyboardManage((FrameLayout) getActivity().getWindow().getDecorView(), keyboardAdapter);
    }


    class ListAdapter extends RecyclerView.Adapter<ListHolder> {

        public ListAdapter() {
        }

        @NonNull
        @Override
        public ListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ListHolder(LayoutInflater.from(getContext()).inflate(R.layout.item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ListHolder viewHolder, int i) {
            viewHolder.editText.setHint("position" + i);
            keyboardManage.add(viewHolder.editText);
            viewHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    keyboardManage.display(viewHolder.editText);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 50;
        }
    }

    public KeyboardManage getKeyboardManage() {
        return keyboardManage;
    }

    class ListHolder extends RecyclerView.ViewHolder {

        private EditText editText;
        private TextView textView;

        public ListHolder(@NonNull View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.item);
            textView = itemView.findViewById(R.id.text);
        }
    }
}
