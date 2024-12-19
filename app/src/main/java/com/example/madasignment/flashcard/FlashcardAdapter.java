package com.example.madasignment.flashcard;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madasignment.R;

import java.util.List;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.CardViewHolder> {

    private Context context;
    private List<FlashcardData> cardDataList;

    public FlashcardAdapter(Context context, List<FlashcardData> cardDataList) {
        this.context = context;
        this.cardDataList = cardDataList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_flashcard_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        FlashcardData cardData = cardDataList.get(position);

        // Set data for the current card
        holder.frontImageView.setImageResource(cardData.getFrontImage());
        holder.backImageView.setImageResource(cardData.getBackImage());

        holder.descriptionImageView.setImageResource(cardData.getDescriptionImage());
        holder.volumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = MediaPlayer.create(context, cardData.getAudioResId());
                mediaPlayer.start();

                // Release MediaPlayer after completion
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
            }
        });
// Ensure ImageButton is always visible
        holder.toggleButton.setVisibility(View.VISIBLE);
        holder.toggleButton.setImageResource(R.drawable.flashcard_drop_button); // Default "down" arrow
        // Reset description card visibility to ensure consistency
        holder.descriptionCard.setVisibility(View.GONE); // Default to hidden
        holder.isDescriptionVisible = false;
        // Set flip animation logic
        holder.cardMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.flipCard();
            }
        });

        // Set description toggle logic
        holder.toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.isDescriptionVisible) {
                    holder.descriptionCard.setVisibility(View.GONE);
                    holder.toggleButton.setImageResource(R.drawable.flashcard_drop_button); // Set to "down"
                } else {
                    holder.descriptionCard.setVisibility(View.VISIBLE);
                    holder.toggleButton.setImageResource(R.drawable.flashcard_up_button); // Set to "up"
                }
                holder.isDescriptionVisible = !holder.isDescriptionVisible;
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardDataList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        ImageButton volumeButton;
        RelativeLayout cardMain;
        CardView frontCard, backCard, descriptionCard;
        ImageView frontImageView, backImageView,descriptionImageView;
        ImageButton toggleButton;
        boolean isFront = true;
        boolean isDescriptionVisible = false;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            cardMain = itemView.findViewById(R.id.card_main);
            frontCard = itemView.findViewById(R.id.card_front);
            backCard = itemView.findViewById(R.id.card_back);
            descriptionCard = itemView.findViewById(R.id.card_description);
            frontImageView = itemView.findViewById(R.id.imageView);
            backImageView = itemView.findViewById(R.id.backImageView);
            descriptionImageView = itemView.findViewById(R.id.descriptionImageView);
            toggleButton = itemView.findViewById(R.id.imageButton);
            volumeButton = itemView.findViewById(R.id.volumeButton);
        }

        // Flip animation logic
        public void flipCard() {
            AnimatorSet setOut = (AnimatorSet) AnimatorInflater.loadAnimator(itemView.getContext(), R.animator.flashcard_flip_out);
            AnimatorSet setIn = (AnimatorSet) AnimatorInflater.loadAnimator(itemView.getContext(), R.animator.flashcard_flip_in);

            setOut.setTarget(isFront ? frontCard : backCard);
            setIn.setTarget(isFront ? backCard : frontCard);

            setOut.start();
            setIn.start();

            isFront = !isFront;
        }
    }
}
