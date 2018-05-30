package com.uniben.attsys;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ILENWABOR DAVID on 30/05/2018.
 */

public class CoursesViewAdapter extends RecyclerView.Adapter<CoursesViewAdapter.CourseViewHolder> {

    String [] courseTitle = {"Power Systems", "Electronic Circuits", "Telecommunications", "Engineering Administration", "Electromagnetism"};
    String [] courseCode = {"EEE 533", "EEE 571", "EEE 573", "PRE 571", "EEE 314"};

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_courses, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        holder.courseTitle.setText(courseTitle[position]);
        holder.courseCode.setText(courseCode[position]);
        holder.avatar.setText(extractFirstLetter(courseTitle[position]));
    }

    @Override
    public int getItemCount() {
        return courseTitle.length;
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder{
        TextView avatar, courseTitle, courseCode;
        public CourseViewHolder(View view){
            super(view);
            avatar = view.findViewById(R.id.text_courses_avatar);
            courseCode = view.findViewById(R.id.course_code_list);
            courseTitle = view.findViewById(R.id.course_name);

        }
    }
    public String extractFirstLetter(String string){
        StringBuilder stringBuilder = new StringBuilder(string);
        return stringBuilder.substring(0,1);
    }
}
