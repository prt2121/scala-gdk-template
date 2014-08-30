package com.pt21.afb

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.{ViewGroup, View}
import android.widget.AdapterView
import com.google.android.glass.app.Card
import com.google.android.glass.media.Sounds
import com.google.android.glass.widget.{CardScrollAdapter, CardScrollView}

/**
 * Created by prt2121 on 8/30/14.
 */
class LandingActivity extends Activity {

  private var mScrollView: Option[CardScrollView] = None
  private var mView: Option[View] = None

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    mView = Some(buildView)
    mScrollView = Some(new CardScrollView(this))
    mScrollView.map { scrollView =>
      scrollView.setAdapter(new CardScrollAdapter() {
        override def getCount: Int = 1

        override def getPosition(item: scala.Any): Int =
          if (item == mView) 0
          else AdapterView.INVALID_POSITION

        override def getView(position: Int, convertView: View, parent: ViewGroup): View = mView.get

        override def getItem(position: Int): AnyRef = mView
      })

      scrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        override def onItemClick(parent: AdapterView[_], view: View, position: Int, id: Long): Unit = {
          val am = getSystemService(Context.AUDIO_SERVICE).asInstanceOf[AudioManager]
          am.playSoundEffect(Sounds.DISALLOWED)
        }

      })
    }
    setContentView(mScrollView.get)
  }

  override def onResume(): Unit = {
    super.onResume()
    mScrollView.map(_.activate)
  }

  override def onPause(): Unit = {
    super.onPause()
    mScrollView.map(_.deactivate)
  }

  def buildView: View = {
    new Card(this).setText("Yeah").getView
  }


}

