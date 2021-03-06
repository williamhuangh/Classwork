class PhotosController < ApplicationController
  def show()
  end
  def new
    if session[:log].nil?
      redirect_to user_path(:login)
    end
    @photo = Photo.new
  end
  def create
    if !params[:photo].nil?
      name = params[:photo][:uploadedPhoto].original_filename
      directory = "app/assets/images"
      path = Rails.root.join(directory, name)
      File.open(path, "wb") do |f|
        f.write(params[:photo][:uploadedPhoto].read)
      end
      newphoto = Photo.new
      newphoto.user_id = session[:log].id
      newphoto.date_time = DateTime.now
      newphoto.file_name = name
      newphoto.save()
      flash[:notice] = "Successfully uploaded"
    end
    redirect_to user_path(session[:log])
  end
end
