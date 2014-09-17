# filters the array and yields only numbers that pass the set of specification set by the
# properties of the hash passed in
def filter (array, properties={})
  array.each do |value|
    passed=true
    properties.each do |key, val|
      if key==:min
        if value<val
          passed=false
        end
      end
      if key==:max
        if value>val
          passed=false
        end
      end
      if key==:odd
        if value%2==0
          passed=false
        end
      end
      if key==:even
        if value%2==1
          passed=false
        end
      end
      if key==:scale
        value*=val
      end
    end
    if passed==true
      yield(value)
    end
  end
end

nums = [6, -5, 319, 400, 18, 94, 11]

filter(nums, :min => 10, :max => 350, :odd => 1, :scale => 2) do |n|
  puts n
end

filter(nums, :max => 0) do |n|
  puts n
end

filter(nums) do |n|
  puts n
end

nums2 = [295, -35, 32, 40, 1, 940, 111]

filter(nums2, :min => 10, :max => 350, :even => 1, :scale => 3) do |n|
  puts n
end

filter(nums2, :min => 200, :odd => 1) do |n|
  puts n
end
