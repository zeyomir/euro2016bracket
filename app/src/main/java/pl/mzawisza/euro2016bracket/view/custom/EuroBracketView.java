package pl.mzawisza.euro2016bracket.view.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import pl.mzawisza.euro2016bracket.R;
import pl.mzawisza.euro2016bracket.model.MatchModel;
import pl.mzawisza.euro2016bracket.model.TeamModel;
import pl.mzawisza.euro2016bracket.utils.SvgDecoder;
import pl.mzawisza.euro2016bracket.utils.SvgDrawableTranscoder;
import pl.mzawisza.euro2016bracket.utils.SvgSoftwareLayerSetter;

public class EuroBracketView extends RelativeLayout {
    private int flagSize = 0;
    private int halfFlagSize = 0;
    private int flagMargin = 0;
    private int connectorWidth = 0;
    private int directionConnectorWidth = 0;

    private int count = 0;

    @BindViews({R.id.crest1_1, R.id.crest1_2, R.id.crest1_3, R.id.crest1_4, R.id.crest1_5, R.id.crest1_6, R.id.crest1_7, R.id.crest1_8, R.id.crest1_9, R.id.crest1_10, R.id.crest1_11, R.id.crest1_12, R.id.crest1_13, R.id.crest1_14, R.id.crest1_15, R.id.crest1_16, R.id.crest2_1, R.id.crest2_2, R.id.crest2_3, R.id.crest2_4, R.id.crest2_5, R.id.crest2_6, R.id.crest2_7, R.id.crest2_8, R.id.crest3_1, R.id.crest3_2, R.id.crest3_3, R.id.crest3_4, R.id.crest4_1, R.id.crest4_2})
    List<ImageView> crests;
    @BindViews({R.id.connector_1_1_1_2, R.id.connector_1_3_1_4, R.id.connector_1_5_1_6, R.id.connector_1_7_1_8, R.id.connector_1_9_1_10, R.id.connector_1_11_1_12, R.id.connector_1_13_1_14, R.id.connector_1_15_1_16, R.id.connector_2_1_2_2, R.id.connector_2_3_2_4, R.id.connector_2_5_2_6, R.id.connector_2_7_2_8, R.id.connector_3_1_3_2, R.id.connector_3_3_3_4})
    List<View> connectors;
    @BindViews({R.id.connector_1_1_1_2_inner, R.id.connector_1_3_1_4_inner, R.id.connector_1_5_1_6_inner, R.id.connector_1_7_1_8_inner, R.id.connector_1_9_1_10_inner, R.id.connector_1_11_1_12_inner, R.id.connector_1_13_1_14_inner, R.id.connector_1_15_1_16_inner, R.id.connector_2_1_2_2_inner, R.id.connector_2_3_2_4_inner, R.id.connector_2_5_2_6_inner, R.id.connector_2_7_2_8_inner, R.id.connector_3_1_3_2_inner, R.id.connector_3_3_3_4_inner})
    List<View> innerConnectors;
    @BindViews({R.id.direction_connector_1_1, R.id.direction_connector_1_2, R.id.direction_connector_1_3, R.id.direction_connector_1_4, R.id.direction_connector_1_5, R.id.direction_connector_1_6, R.id.direction_connector_1_7, R.id.direction_connector_1_8, R.id.direction_connector_2_1, R.id.direction_connector_2_2, R.id.direction_connector_2_3, R.id.direction_connector_2_4, R.id.direction_connector_3_1, R.id.direction_connector_3_2})
    List<View> directionConnectors;
    @BindView(R.id.rendered)
    TouchImageView rendered;
    @BindView(R.id.drawing)
    View drawing;

    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;
    private Settings settings;

    private SvgSoftwareLayerSetter.CompletionListener completionListener = new SvgSoftwareLayerSetter.CompletionListener() {
        @Override
        public void report(int count) {
            if (count == EuroBracketView.this.count && readyToRender)
                post(new Runnable() {
                    @Override
                    public void run() {
                        render();
                    }
                });
        }
    };
    private boolean readyToRender;

    public EuroBracketView(Context context) {
        super(context);
        init(context);
    }

    public EuroBracketView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EuroBracketView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EuroBracketView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.c_bracket, this);
        ButterKnife.bind(this);
        requestBuilder = Glide.with(context)
                .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .placeholder(R.drawable.unknown_flag)
                .error(R.drawable.no_flag)
                .dontAnimate()
                .listener(new SvgSoftwareLayerSetter<Uri>(completionListener))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        updateDimensions();
    }

    private void updateDimensions() {
        int height = getMeasuredHeight();
        flagSize = height / 16;
        halfFlagSize = flagSize / 2;
        connectorWidth = flagSize / 5;
        directionConnectorWidth = connectorWidth * 4;
        flagMargin = flagSize / 6;

        for (ImageView crest : crests) {
            RelativeLayout.LayoutParams layoutParams = (LayoutParams) crest.getLayoutParams();
            layoutParams.height = flagSize;
            layoutParams.width = flagSize;
            crest.setPadding(0, flagMargin, 0, flagMargin);
        }

        for (View connector : connectors) {
            RelativeLayout.LayoutParams layoutParams = (LayoutParams) connector.getLayoutParams();
            layoutParams.width = connectorWidth;
        }

        for (View innerConnector : innerConnectors) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) innerConnector.getLayoutParams();
            layoutParams.height = flagSize;
        }

        for (int i = 0; i < directionConnectors.size(); i++) {
            View directionConnector = directionConnectors.get(i);
            RelativeLayout.LayoutParams layoutParams = (LayoutParams) directionConnector.getLayoutParams();
            layoutParams.width = directionConnectorWidth;
            if (i % 2 == 0) {
                layoutParams.setMargins(0, 0, 0, halfFlagSize);
            } else {
                layoutParams.setMargins(0, halfFlagSize, 0, 0);
            }
        }

        invalidate();
    }

    private void setFirstDay(List<MatchModel> matchModels) {
        for (int i = 0; i < 8; i++) {
            MatchModel matchModel = matchModels.get(i);
            if (matchModel == null)
                continue;

            int crestId = 2 * i;

            loadCrests(matchModel, crestId);
        }
    }

    private void setSecondDay(List<MatchModel> matchModels) {
        for (int i = 0; i < 4; i++) {
            MatchModel matchModel = matchModels.get(i);
            if (matchModel == null)
                continue;

            int crestId = 2 * i + 16;

            loadCrests(matchModel, crestId);
        }
    }

    private void setThirdDay(List<MatchModel> matchModels) {
        for (int i = 0; i < 2; i++) {
            MatchModel matchModel = matchModels.get(i);
            if (matchModel == null)
                continue;

            int crestId = 2 * i + 24;

            loadCrests(matchModel, crestId);
        }
    }

    private void setFourthDay(MatchModel matchModel) {
        if (matchModel == null)
            return;
        int crestId = 28;
        loadCrests(matchModel, crestId);
    }

    private void loadCrests(MatchModel matchModel, int crestId) {
        String teamA = matchModel.teamA;
        String teamB = matchModel.teamB;
        if (teamA != null) {
            TeamModel teamModel = settings.teams.get(teamA);
            requestBuilder
                    .load(Uri.parse(teamModel.crestUri))
                    .into(crests.get(crestId));
            count++;
        }
        if (teamB != null) {
            TeamModel teamModel = settings.teams.get(teamB);
            requestBuilder
                    .load(Uri.parse(teamModel.crestUri))
                    .into(crests.get(crestId + 1));
            count++;
        }
    }

    private void updateView() {
        rendered.setVisibility(GONE);
        drawing.setVisibility(VISIBLE);
        readyToRender = false;
        setFirstDay(settings.firstDay);
        setSecondDay(settings.secondDay);
        setThirdDay(settings.thirdDay);
        setFourthDay(settings.fourthDay);
        readyToRender = true;
    }

    private void render() {
        Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawing.draw(canvas);
        rendered.setImageBitmap(bitmap);
        rendered.setVisibility(VISIBLE);
        drawing.setVisibility(GONE);
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
        count = 0;
        updateView();
    }

    public static class SettingsBuilder {
        private Map<String, TeamModel> teams;
        private List<MatchModel> firstDay, secondDay, thirdDay;
        private MatchModel lastDay;


        public SettingsBuilder withTeams(Map<String, TeamModel> teams) {
            this.teams = teams;
            return this;
        }

        public SettingsBuilder withFirstDay(List<MatchModel> matches) {
            this.firstDay = matches;
            return this;
        }

        public SettingsBuilder withSecondDay(List<MatchModel> matches) {
            this.secondDay = matches;
            return this;
        }

        public SettingsBuilder withThirdDay(List<MatchModel> matches) {
            this.thirdDay = matches;
            return this;
        }

        public SettingsBuilder withFourthDay(MatchModel match) {
            this.lastDay = match;
            return this;
        }

        public Settings build() {
            return new Settings(this);
        }
    }

    public static class Settings {
        private final Map<String, TeamModel> teams;
        private final List<MatchModel> firstDay, secondDay, thirdDay;
        private final MatchModel fourthDay;

        private Settings(SettingsBuilder settingsBuilder) {
            if (settingsBuilder.teams == null) {
                teams = new HashMap<>();
            } else {
                teams = settingsBuilder.teams;
            }

            if (settingsBuilder.firstDay == null) {
                firstDay = new ArrayList<>(8);
                Collections.fill(firstDay, null);
            } else {
                firstDay = settingsBuilder.firstDay;
            }

            if (settingsBuilder.secondDay == null) {
                secondDay = new ArrayList<>(4);
                Collections.fill(secondDay, null);
            } else {
                secondDay = settingsBuilder.secondDay;
            }

            if (settingsBuilder.thirdDay == null) {
                thirdDay = new ArrayList<>(2);
                Collections.fill(thirdDay, null);
            } else {
                thirdDay = settingsBuilder.thirdDay;
            }

            fourthDay = settingsBuilder.lastDay;
        }
    }
}
