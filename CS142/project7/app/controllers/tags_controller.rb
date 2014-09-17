class TagsController < ApplicationController
	def index()
		@photo = Photo.find(params[:photo_id])
		@tags = Tag.where(:photo_id => params[:photo_id])
	end
	def new
		if session[:log].nil?
			redirect_to user_path(:login)
		end
		@photo = Photo.find(params[:photo_id])
		@tag = Tag.new
		@users = User.find(:all)
		@usernames = @users.map do |u|
			u.first_name + " " + u.last_name
		end
	end
	def create
		if session[:log].nil?
			redirect_to user_path(:login)
		end
		@tag = Tag.new(tag_params(params[:tag]))
		first_name = (params[:tag][:user_id].split(' '))[0]
		@tag.user_id = User.find_by_first_name(first_name).id
		@tag.photo_id = params[:photo_id]
		if @tag.valid?
			@tag.save()
			flash[:notice] = "Successfully Tagged"
			redirect_to photo_tags_path(params[:photo_id])
		else
			flash[:notice] = "Nothing tagged."
			@photo = Photo.find(params[:photo_id])
			@tag = Tag.new
			@users = User.find(:all)
			@usernames = @users.map do |u|
				u.first_name + " " + u.last_name
			end
			render :new
		end
	end
	def tag_params(params)
		params.permit(:photo_id, :user_id, :x_value, :y_value, :width, :height)
	end
end
