class CommentsController < ApplicationController
  def new
    @photo = Photo.find(params[:photo_id])
    @comment = Comment.new
    if session[:log].nil?
      redirect_to user_path(:login)
    end
  end
  def create
    @newcomment = params[:comment]
    comment = Comment.new
    comment.photo_id = params[:photo_id]
    comment.user_id = session[:log].id
    comment.date_time = DateTime.now
    comment.comment = @newcomment[:comment]
    if comment.valid?
      comment.save()
      redirect_to user_path(Photo.find(params[:photo_id]).user)
    else
      @photo = Photo.find(params[:photo_id])
      @comment = comment
      render :new
    end
  end
end
