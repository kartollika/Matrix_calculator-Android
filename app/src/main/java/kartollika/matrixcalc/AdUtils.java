package kartollika.matrixcalc;

import android.app.Activity;
import android.view.View;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.RewardedVideoCallbacks;

final class AdUtils {

    public static void loadAd(Activity activity) {
        if (!GlobalValues.canShowVideo()) {
            activity.findViewById(R.id.adCard).setVisibility(View.GONE);
            return;
        } else {
            activity.findViewById(R.id.adCard).setVisibility(View.VISIBLE);
        }

        if (BuildConfig.BUILD_TYPE == "debug") {
            Appodeal.setTesting(true);
        }


        Appodeal.setBannerViewId(R.id.appodealBannerView);
        Appodeal.setBannerAnimation(false);
        Appodeal.initialize(activity, GlobalValues.appKey, Appodeal.BANNER_VIEW
                | Appodeal.REWARDED_VIDEO);
        Appodeal.show(activity, Appodeal.BANNER_VIEW);
    }

    static void loadVideo(final Activity activity) {
        Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {
            @Override
            public void onRewardedVideoLoaded() {
            }

            @Override
            public void onRewardedVideoFailedToLoad() {
                Utilities.createShortToast(activity.getApplicationContext(),
                        activity.getResources().getString(R.string.failed_watch_ad_connection_problem)).show();
            }

            @Override
            public void onRewardedVideoShown() {
            }

            @Override
            public void onRewardedVideoFinished(int i, String s) {
                GlobalValues.setEstimatedTimeToWatchVideo(System.currentTimeMillis()
                        + GlobalValues.ESTIMATED_TIME);
            }

            @Override
            public void onRewardedVideoClosed(boolean b) {
                Utilities.createShortToast(activity.getApplicationContext(),
                        activity.getResources().getString(R.string.succ_blocked_banners)).show();
            }
        });
        Appodeal.show(activity, Appodeal.REWARDED_VIDEO);
    }
}
