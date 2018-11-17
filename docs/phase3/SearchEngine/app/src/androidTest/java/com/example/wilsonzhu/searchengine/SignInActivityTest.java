package com.example.wilsonzhu.searchengine;

import com.example.wilsonzhu.searchengine.Activity.SignInActivity;
import com.google.android.gms.common.SignInButton;

import android.content.Intent;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SignInActivityTest {
    @Rule
    public ActivityTestRule<SignInActivity> activityRule =
            new ActivityTestRule<>(SignInActivity.class, true, false);


    @SmallTest
    public void testUIVisible() {
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
        SignInActivity activity =  activityRule.getActivity();

        // 0 for true
        TextView display_name = activity.findViewById(R.id.email_display_name);
        assertEquals(display_name.getText(), "");
        assertEquals(display_name.getVisibility(), 0);

        TextView email = activity.findViewById(R.id.email);
        assertEquals(email.getText(), "");
        assertEquals(email.getVisibility(), 0);

        ImageView display_picture = activity.findViewById(R.id.email_image_pill);
        assertEquals(display_picture.getVisibility(), 0);

        SignInButton googleSignInButton = activity.findViewById(R.id.googleSignInButton);
        assertEquals(googleSignInButton.getVisibility(), 0);
    }
}
