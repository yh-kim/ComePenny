package com.enterpaper.comepenny.util;

import com.enterpaper.comepenny.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;

public class ApplicationClass extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		init(this);
	}
	
	public void init(Context ctx){
		//image �ɼ� ����
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.mipmap.ic_launcher)		//loading���϶� ������ Image
		.showImageForEmptyUri(R.mipmap.ic_launcher)	//Image�� ��û������ ������ ������ Image
		.showImageOnFail(R.mipmap.ic_launcher)				//Image�� ��û������ ������ ������ �� ������ Image
		.cacheInMemory(true)			//memory cache ����ϰڴ�
		.cacheOnDisc(true)				//File cache ���
		.considerExifParams(true)	//���� �����̸� ���η� ���λ����̸� ���η�
		.build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ctx)
		.threadPriority(Thread.NORM_PRIORITY - 2)		//�������� �켱������ nomal ���� 2����
		.denyCacheImageMultipleSizesInMemory()			
		.diskCacheFileNameGenerator(new Md5FileNameGenerator())		//File cache�� �̸��� �����ϴ� ���
		.tasksProcessingOrder(QueueProcessingType.LIFO)	//Thread Pool�� ��� �۵���ų����(������������ ���)
		.defaultDisplayImageOptions(options)
		.build();
		
		ImageLoader.getInstance().init(config);
		
	}

}
