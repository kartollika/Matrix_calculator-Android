package kartollika.matrixcalc;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import static kartollika.matrixcalc.App.BLOCKING_BANNERS;
import static kartollika.matrixcalc.App.BLOCKING_INTERSITIALS;
import static kartollika.matrixcalc.App.CUR_REWARD;
import static kartollika.matrixcalc.App.TARGET_DEVICE_ID;

final class AdUtils {

    static RewardedVideoAd rewardedVideoAd;

    private static InterstitialAd interstitialAd;
    private static Resources resources = null;

    public static void initResources(Context context) {
        resources = context.getResources();
    }

    public static void initRewardedVideo(final Activity activity) {
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                loadRewardVideoAd();
                CUR_REWARD = "";
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                switch (App.CUR_REWARD) {
                    case "BANNERS":
                        App.setEstimatedTimeBanners(System.currentTimeMillis()
                                + rewardItem.getAmount() * BLOCKING_BANNERS);
                        break;
                    case "INTERSTITIAL":
                        App.setEstimatedTimeInterstitial(System.currentTimeMillis()
                                + rewardItem.getAmount() * BLOCKING_INTERSITIALS);
                }
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Toast.makeText(activity, R.string.failed_watch_ad_connection_problem,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRewardedVideoCompleted() {
                switch (App.CUR_REWARD) {
                    case "BANNERS":
                        turnOnOffAdCard(activity, View.GONE);
                        Toast.makeText(activity, resources.getString(R.string.succ_blocked_banners,
                                BLOCKING_BANNERS), Toast.LENGTH_LONG).show();
                        break;
                    case "INTERSTITIAL":
                        Toast.makeText(activity, resources.getString(R.string.succ_blocked_interstitials,
                                BLOCKING_INTERSITIALS), Toast.LENGTH_LONG).show();
                }
            }
        });
        loadRewardVideoAd();
    }

    public static void initBanner(final Activity activity) {
        if (App.canShowNewBannersVideo()) {
            turnOnOffAdCard(activity, View.VISIBLE);
            final AdView adView = activity.findViewById(R.id.adView);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                    if (App.canShowNewBannersVideo()) {
                        activity.findViewById(R.id.adText).setVisibility(View.VISIBLE);
                        //tryLoadBanner(adView);
                    } else {
                        turnOnOffAdCard(activity, View.GONE);
                    }
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    if (App.canShowNewBannersVideo()) {
                        turnOnOffAdCard(activity, View.VISIBLE);
                    } else {
                        turnOnOffAdCard(activity, View.GONE);
                    }
                    activity.findViewById(R.id.adText).setVisibility(View.GONE);
                }
            });
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(TARGET_DEVICE_ID).build();
            adView.loadAd(adRequest);
        } else {
            turnOnOffAdCard(activity, View.GONE);
        }
    }

    public static void tryLoadBanner(AdView adView) {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(TARGET_DEVICE_ID).build();
        adView.loadAd(adRequest);
    }

    private static void turnOnOffAdCard(Activity activity, int STATUS) {
        activity.findViewById(R.id.adCard).setVisibility(STATUS);
    }

    public static void initInterstitialAd(final Activity activity) {
        interstitialAd = new InterstitialAd(activity);
        interstitialAd.setAdUnitId(resources.getString(R.string.interstitial_ad));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                loadInterstitialAd();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                initInterstitialAd(activity);
            }
        });
        loadInterstitialAd();
    }

    private static void loadRewardVideoAd() {
        rewardedVideoAd.loadAd(resources.getString(R.string.reward_video_ad),
                new AdRequest.Builder().addTestDevice(TARGET_DEVICE_ID).build());
    }

    private static void loadInterstitialAd() {
        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice(TARGET_DEVICE_ID).build());
    }

    public static void showRewardVideoAd(Context context) {
        if (rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.show();
        } else {
            loadRewardVideoAd();
            Toast.makeText(context, R.string.try_again_open_reward_video, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showInterstitialAd() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    public static void destroyBanner(AdView adView) {
        adView.destroy();
    }
}
